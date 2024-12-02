"use client";

import React, { useEffect, useRef } from "react";
import dynamic from "next/dynamic";

// 타입 정의
interface PostDto {
    id: number;
    title: string;
    body: string;
    published: boolean;
    listed: boolean;
}

interface Props {
    id: number;
    post: PostDto;
}

// 유틸리티 함수
const getUrlParams = (url: string): Record<string, string> => {
    const params: Record<string, string> = {};
    if (url.includes('?')) {
        const queryString = url.split('?')[1];
        const pairs = queryString.split('&');
        pairs.forEach(pair => {
            const [key, value] = pair.split('=');
            params[key] = value;
        });
    }
    return params;
};

const filterObjectKeys = (obj: any, allowedKeys: string[]) => {
    const filtered: any = {};
    for (const key of allowedKeys) {
        if (obj && obj[key] !== undefined) {
            filtered[key] = obj[key];
        }
    }
    return filtered;
};

// 동적 임포트를 위한 설정
const Editor = dynamic(() => import('@toast-ui/editor'), {
    ssr: false,
    loading: () => <p>Loading editor...</p>
});

export default function ClientPage({ id, post }: Props) {
    const editorRef = useRef<HTMLDivElement>(null);
    const body = post.body || "";
    const viewer = false;
    const height = "500px";
    const apiUrl = process.env.NEXT_PUBLIC_CORE_API_BASE_URL || '';

    const saveBody = async (editor: any) => {
        try {
            const response = await fetch(`${apiUrl}/posts/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    title: post.title,
                    body: editor.getMarkdown(),
                    published: post.published,
                    listed: post.listed,
                }),
            });

            if (!response.ok) {
                throw new Error('Failed to save');
            }

            alert('저장되었습니다.');
        } catch (error) {
            console.error('Save error:', error);
            alert('저장에 실패했습니다.');
        }
    };

    const loadEditor = async () => {
        if (!editorRef.current) return;

        try {
            const [
                { default: Editor },
                { default: codeSyntaxHighlight },
                { default: colorSyntax },
                { default: tableMergedCell },
                { default: uml },
            ] = await Promise.all([
                import('@toast-ui/editor'),
                import('@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight-all'),
                import('@toast-ui/editor-plugin-color-syntax'),
                import('@toast-ui/editor-plugin-table-merged-cell'),
                import('@toast-ui/editor-plugin-uml'),
            ]);

            const umlOptions = {
                rendererURL: "http://www.plantuml.com/plantuml/svg/",
            };

            // 플러그인 설정
            const plugins = {
                configPlugin: () => ({
                    toHTMLRenderers: {
                        config: () => [
                            { type: "openTag", tagName: "div", outerNewLine: true },
                            { type: "html", content: "" },
                            { type: "closeTag", tagName: "div", outerNewLine: true },
                        ],
                    },
                }),

                hidePlugin: () => ({
                    toHTMLRenderers: {
                        hide: () => [
                            { type: "openTag", tagName: "div", outerNewLine: true },
                            { type: "html", content: "" },
                            { type: "closeTag", tagName: "div", outerNewLine: true },
                        ],
                    },
                }),

                pptPlugin: () => ({
                    toHTMLRenderers: {
                        ppt: () => [
                            { type: "openTag", tagName: "div", outerNewLine: true },
                            { type: "html", content: "" },
                            { type: "closeTag", tagName: "div", outerNewLine: true },
                        ],
                    },
                }),

                youtubePlugin: () => ({
                    toHTMLRenderers: {
                        youtube: (node: any) => {
                            const html = renderYoutube(node.literal);
                            return [
                                { type: "openTag", tagName: "div", outerNewLine: true },
                                { type: "html", content: html },
                                { type: "closeTag", tagName: "div", outerNewLine: true },
                            ];
                        },
                    },
                }),

                codepenPlugin: () => ({
                    toHTMLRenderers: {
                        codepen: (node: any) => {
                            const html = renderCodepen(node.literal);
                            return [
                                { type: "openTag", tagName: "div", outerNewLine: true },
                                { type: "html", content: html },
                                { type: "closeTag", tagName: "div", outerNewLine: true },
                            ];
                        },
                    },
                }),
            };

            function renderYoutube(url: string) {
                url = url.replace(/^(https?:\/\/)?(www\.)?(youtube\.com\/watch\?v=|youtu\.be\/)/i, '');
                const urlParams = getUrlParams(url);
                const maxWidth = urlParams["max-width"] || "500";
                const marginLeft = urlParams["margin-left"] || "auto";
                const marginRight = urlParams["margin-right"] || "auto";
                const youtubeId = url.split('?')[0];

                return `
                    <div style="max-width:${maxWidth}px; margin-left:${marginLeft}; margin-right:${marginRight};" class="aspect-[16/9] relative">
                        <iframe 
                            class="absolute top-0 left-0 w-full" 
                            width="100%" 
                            height="100%" 
                            src="https://www.youtube.com/embed/${youtubeId}"
                            allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" 
                            allowfullscreen
                        ></iframe>
                    </div>
                `;
            }

            function renderCodepen(url: string) {
                const urlParams = getUrlParams(url);
                const height = urlParams.height || "400";
                let width = urlParams.width || "100%";
                if (!width.includes("px") && !width.includes("%")) width += "px";

                const iframeUri = url.split('#')[0];

                return `
                    <iframe 
                        height="${height}" 
                        style="width: ${width};" 
                        src="${iframeUri}" 
                        allowtransparency="true" 
                        allowfullscreen="true"
                    ></iframe>
                `;
            }

            const editorConfig = {
                plugins: [
                    codeSyntaxHighlight,
                    colorSyntax,
                    tableMergedCell,
                    [uml, umlOptions],
                    plugins.configPlugin,
                    plugins.hidePlugin,
                    plugins.pptPlugin,
                    plugins.youtubePlugin,
                    plugins.codepenPlugin,
                ],
                linkAttributes: {
                    target: "_blank",
                },
                customHTMLRenderer: {
                    heading: (node: any, { entering, getChildrenText }: any) => ({
                        type: entering ? "openTag" : "closeTag",
                        tagName: `h${node.level}`,
                        attributes: {
                            id: getChildrenText(node).trim().replaceAll(" ", "-"),
                        },
                    }),
                    htmlBlock: {
                        iframe: (node: any) => {
                            const newAttrs = filterObjectKeys(node.attrs, [
                                "src", "width", "height", "allow",
                                "allowfullscreen", "frameborder", "scrolling",
                            ]);

                            return [
                                {
                                    type: "openTag",
                                    tagName: "iframe",
                                    outerNewLine: true,
                                    attributes: newAttrs,
                                },
                                { type: "html", content: node.childrenHTML },
                                { type: "closeTag", tagName: "iframe", outerNewLine: false },
                            ];
                        },
                    },
                },
                initialValue: body,
            };

            const editor = new Editor({
                el: editorRef.current,
                height,
                viewer,
                ...editorConfig,
            });

            // 저장 명령어 추가
            editor.addCommand("markdown", "saveBody", () => {
                saveBody(editor);
                return true;
            });

            // 툴바 아이템 추가
            editor.insertToolbarItem(
                { groupIndex: 0, itemIndex: 0 },
                {
                    name: "saveBody",
                    tooltip: "저장(Ctrl + s, Cmd + s)",
                    className: "!text-[20px]",
                    text: "S",
                    command: "saveBody",
                }
            );
        } catch (error) {
            console.error('Editor loading error:', error);
        }
    };

    useEffect(() => {
        loadEditor();
    }, []);

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">{post.title}</h1>
            <div ref={editorRef} className="border rounded-lg"></div>
        </div>
    );
}