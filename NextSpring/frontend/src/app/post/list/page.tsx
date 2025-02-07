export default async function Page() {
    const response = await fetch("http://localhost:9090/api/v1/posts");
    const body = await response.json();

    return (
        <div>
            <div>
                <div>currentPageNumber: {body.pageable.pageNumber}</div>
                <div>pageSize: {body.pageable.pageSize}</div>
                <div>totalPages: {body.pageable.totalPages}</div>
                <div>totalItems: {body.pageable.totalElements}</div>
            </div>

            <hr />

            <ul>
                {body.content.map((item: any) => (
                    <li key={item.id} className="border-[2px] border-[red] my-3">
                        <div>id : {item.id}</div>
                        <div>createDate : {item.createDate}</div>
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
