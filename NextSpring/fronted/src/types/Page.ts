// Sort 타입 정의
type Sort = {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
};

// Pageable 타입 정의
type Pageable = {
    pageNumber: number;
    pageSize: number;
    sort: Sort;
    offset: number;
    paged: boolean;
    unpaged: boolean;
};

// Page 타입 정의
type Page<T> = {
    content: T[];
    pageable: Pageable;
    last: boolean;
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
    sort: Sort;
    first: boolean;
    numberOfElements: number;
    empty: boolean;
};
export default Page;