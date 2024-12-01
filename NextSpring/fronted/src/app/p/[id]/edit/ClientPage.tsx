"use client";

import { components } from "@/lib/backend/apiV1/schema";
import client from "@/lib/openapi_fetch";

import { filterObjectKeys, getUrlParams } from "@/lib/utils";
import "@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight.css";
import "@toast-ui/editor-plugin-color-syntax/dist/toastui-editor-plugin-color-syntax.css";
import "@toast-ui/editor-plugin-table-merged-cell/dist/toastui-editor-plugin-table-merged-cell.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import "prismjs/themes/prism.css";
import { useEffect, useRef } from "react";
import "tui-color-picker/dist/tui-color-picker.css";

export default function ClientPage({
                                       id,
                                       post,
                                   }: {
    id: number;
    post: components["schemas"]["PostDto"];
}) {
    const editorRef = useRef<HTMLDivElement>(null);
    const body = post.body || "";
    const viewer = false;
    const height = "500px";
    const apiUrl = `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}`;
    const saveBody = (editor: any) => {
        client.PUT(apiUrl+"/posts/{id}", {
            params: { path: { id } },
            body: {
                title: post.title,
                body: editor.getMarkdown(),
                published: post.published,
                listed: post.listed,
            },
        });
    };

    const loadEditor = async () => {
        const [
            { default: Editor },
            { default: codeSyntaxHighlight },
            { default: colorSyntax },
            { default: tableMergedCell },
            { default: uml },
        ] = await Promise.all([
            // @ts-ignore
            import("@toast-ui/editor"),
            import(
                // @ts-ignore
                "@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight-all"
                ),
            import("@toast-ui/editor-plugin-color-syntax"),
            import("@toast-ui/editor-plugin-table-merged-cell"),
            import("@toast-ui/editor-plugin-uml"),
        ]);

        const umlOptions = {
            rendererURL: "http://www.plantuml.com/plantuml/svg/",
        };

        function configPlugin() {
            const toHTMLRenderers = {
                config(node: any) {
                    return [
                        { type: "openTag", tagName: "div", outerNewLine: true },
                        { type: "html", content: "" },
                        { type: "closeTag", tagName: "div", outerNewLine: true },
                    ];
                },
            };

            return { toHTMLRenderers };
        }

        function hidePlugin() {
            const toHTMLRenderers = {
                hide(node: any) {
                    return [
                        { type: "openTag", tagName: "div", outerNewLine: true },
                        { type: "html", content: "" },
                        { type: "closeTag", tagName: "div", outerNewLine: true },
                    ];
                },
            };

            return { toHTMLRenderers };
        }

        function pptPlugin() {
            const toHTMLRenderers = {
                ppt(node: any) {
                    return [
                        { type: "openTag", tagName: "div", outerNewLine: true },
                        { type: "html", content: "" },
                        { type: "closeTag", tagName: "div", outerNewLine: true },
                    ];
                },
            };

            return { toHTMLRenderers };
        }

        function youtubePlugin() {
            const toHTMLRenderers = {
                youtube(node: any) {
                    const html = renderYoutube(node.literal);

                    return [
                        { type: "openTag", tagName: "div", outerNewLine: true },
                        { type: "html", content: html },
                        { type: "closeTag", tagName: "div", outerNewLine: true },
                    ];
                },
            };

            function renderYoutube(url: string) {
                url = url.replace("https://www.youtube.com/watch?v=", "");
                url = url.replace("http://www.youtube.com/watch?v=", "");
                url = url.replace("www.youtube.com/watch?v=", "");
                url = url.replace("youtube.com/watch?v=", "");
                url = url.replace("https://youtu.be/", "");
                url = url.replace("http://youtu.be/", "");
                url = url.replace("youtu.be/", "");

                let urlParams = getUrlParams(url);

                let width = "100%";
                let height = "100%";

                let maxWidth = "500";

                if (urlParams["max-width"]) {
                    maxWidth = urlParams["max-width"];
                }

                let ratio = "aspect-[16/9]";

                let marginLeft = "auto";

                if (urlParams["margin-left"]) {
                    marginLeft = urlParams["margin-left"];
                }

                let marginRight = "auto";

                if (urlParams["margin-right"]) {
                    marginRight = urlParams["margin-right"];
                }

                let youtubeId = url;

                if (youtubeId.indexOf("?") !== -1) {
                    let pos = url.indexOf("?");
                    youtubeId = youtubeId.substring(0, pos);
                }

                return (
                    '<div style="max-width:' +
                    maxWidth +
                    "px; margin-left:" +
                    marginLeft +
                    "; margin-right:" +
                    marginRight +
                    ';" class="' +
                    ratio +
                    ' relative"><iframe class="absolute top-0 left-0 w-full" width="' +
                    width +
                    '" height="' +
                    height +
                    '" src="https://www.youtube.com/embed/' +
                    youtubeId +
                    '" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe></div>'
                );
            }

            return { toHTMLRenderers };
        }

        function codepenPlugin() {
            const toHTMLRenderers = {
                codepen(node: any) {
                    const html = renderCodepen(node.literal);

                    return [
                        { type: "openTag", tagName: "div", outerNewLine: true },
                        { type: "html", content: html },
                        { type: "closeTag", tagName: "div", outerNewLine: true },
                    ];
                },
            };

            function renderCodepen(url: string) {
                const urlParams = getUrlParams(url);

                let height = "400";

                if (urlParams.height) {
                    height = urlParams.height;
                }

                let width = "100%";

                if (urlParams.width) {
                    width = urlParams.width;
                }

                if (!width.includes("px") && !width.includes("%")) {
                    width += "px";
                }

                let iframeUri = url;

                if (iframeUri.indexOf("#") !== -1) {
                    let pos = iframeUri.indexOf("#");
                    iframeUri = iframeUri.substring(0, pos);
                }

                return (
                    '<iframe height="' +
                    height +
                    '" style="width: ' +
                    width +
                    ';" title="" src="' +
                    iframeUri +
                    '" allowtransparency="true" allowfullscreen="true"></iframe>'
                );
            }

            return { toHTMLRenderers };
        }

        const editorConfig = {
            plugins: [
                codeSyntaxHighlight,
                colorSyntax,
                tableMergedCell,
                [uml, umlOptions],
                configPlugin,
                hidePlugin,
                pptPlugin,
                youtubePlugin,
                codepenPlugin,
            ],
            linkAttributes: {
                target: "_blank",
            },
            customHTMLRenderer: {
                heading(node: any, { entering, getChildrenText }: any) {
                    return {
                        type: entering ? "openTag" : "closeTag",
                        tagName: `h${node.level}`,
                        attributes: {
                            id: getChildrenText(node).trim().replaceAll(" ", "-"),
                        },
                    };
                },
                htmlBlock: {
                    iframe(node: any) {
                        const newAttrs = filterObjectKeys(node.attrs, [
                            "src",
                            "width",
                            "height",
                            "allow",
                            "allowfullscreen",
                            "frameborder",
                            "scrolling",
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
            el: editorRef.current as HTMLElement,
            height: height,
            viewer: viewer,
            ...editorConfig,
        });

        editor.addCommand("markdown", "saveBody", () => {
            saveBody(editor);

            return true;
        });

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
    };

    useEffect(() => {
        loadEditor();
    }, []);

    return (
        <div>
            <h1>{post.title}</h1>
            <div ref={editorRef}></div>
        </div>
    );
}
