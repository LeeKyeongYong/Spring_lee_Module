import type { components } from "@/lib/backend/apiV1/schema";
type PostDto = components["schemas"]["PostDto"];
type PageDtoPostDto = components["schemas"]["PageDtoPostDto"];

export default async function Page({
                                       searchParams,
                                   }: {
    searchParams: {
        searchKeywordType?: "title" | "content";
        searchKeyword?: string;
    };
}) {
    const { searchKeyword = "", searchKeywordType = "title" } = searchParams;

    const response = await fetch(
        `http://localhost:8080/api/v1/posts?searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`
    );

    const body:PageDtoPostDto  = await response.json();


    return (
        <div>

            <form>
                <select name="searchKeywordType" defaultValue={searchKeywordType}>
                    <option value="title">제목</option>
                    <option value="content">내용</option>
                </select>
                <input type="text" name="searchKeyword" defaultValue={searchKeyword} />
                <button type="submit">검색</button>
            </form>

            <div>
                <div>currentPageNumber: {body.pageable.pageNumber}</div>
                <div>pageSize: {body.pageable.pageSize}</div>
                <div>totalPages: {body.pageable.totalPages}</div>
                <div>totalItems: {body.pageable.totalElements}</div>
            </div>

            <hr />

            <ul>
                {body.content.map((item: PostDto) => (
                    <li key={item.id} className="border-[2px] border-[red] my-3">
                        <div>id : {item.id}</div>
                        <div>createDate : {item.}</div>
                        <div>modifyDate : {item.modifyDate}</div>
                        <div>authorId : {item.authorId}</div>
                        <div>authorName : {item.authorName}</div>
                        <div>title : {item.title}</div>
                        <div>published : {`${item.published}`}</div>
                        <div>listed : {`${item.listed}`}</div>
                    </li>
                ))}
            </ul>
        </div>
    );
}
