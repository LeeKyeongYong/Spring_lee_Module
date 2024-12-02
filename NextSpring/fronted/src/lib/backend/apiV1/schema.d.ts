export interface paths {
    "/api/v1/members/me": {
        get: {
            responses: {
                200: {
                    content: {
                        "application/json": {
                            data: {
                                id: number;
                                username: string;
                                nickname: string;
                            };
                        };
                    };
                };
            };
        };
    };
    "/api/v1/members/login": {
        post: {
            requestBody: {
                content: {
                    "application/json": {
                        username: string;
                        password: string;
                    };
                };
            };
            responses: {
                200: {
                    content: {
                        "application/json": {
                            data: {
                                item: {
                                    id: number;
                                    username: string;
                                    nickname: string;
                                };
                            };
                        };
                    };
                };
            };
        };
    };
    "/api/v1/members/logout": {
        post: {
            responses: {
                200: {
                    content: {
                        "application/json": {
                            message: string;
                        };
                    };
                };
            };
        };
    };
}