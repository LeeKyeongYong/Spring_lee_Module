export type Post = {
    id: number;
    createDate: string;
    modifyDate: string;
    title: string;
    body: string;
    authorId: number;
    authorName: string;
    authorProfileImgUrl: string;
    actorCanRead: boolean | null;
    actorCanModify: boolean | null;
    actorCanDelete: boolean | null;
};