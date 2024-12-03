export function filterObjectKeys(
    obj: Record<string, any>,
    keysToKeep: string[]
) {
    return Object.keys(obj)
        .filter((key) => keysToKeep.includes(key))
        .reduce((newObj, key) => {
            newObj[key] = obj[key];
            return newObj;
        }, {} as Record<string, any>);
}

export function getUrlParams(url: string): Record<string, string> {
    // 공백 제거 및 HTML 엔티티 정리
    url = url.trim().replaceAll("&amp;", "&");

    // URL 객체 생성
    const urlObj = new URL(url, window.location.href);

    const params: Record<string, string> = {};

    // URLSearchParams 객체를 사용하여 쿼리 파라미터 추출
    urlObj.searchParams.forEach((value, key) => {
        params[key] = value;
    });

    return params;
}