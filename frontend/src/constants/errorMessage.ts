interface ErrorHandler {
    [key: string]: { msg: string; errorCode: number | null };
}

export const ERROR_MESSAGE: ErrorHandler = Object.freeze({
    BAD_REQUEST: { msg: '잘못된 요청입니다.', errorCode: 400 },
    UNAUTHORIZED: { msg: '인증이 필요합니다.', errorCode: 401 },
    FORBIDDEN: { msg: '접근 권한이 없습니다.', errorCode: 403 },
    NOT_FOUND: { msg: '리소스를 찾을 수 없습니다.', errorCode: 404 },
    SERVER_ERROR: { msg: '서버 오류가 발생했습니다.', errorCode: 500 },
    UNKNOWN_ERROR: {
        msg: '알 수 없는 오류가 발생했습니다.',
        errorCode: null
    },
    NETWORK_ERROR: {
        msg: '서버 연결이 원활하지 않습니다.',
        errorCode: null
    }
});
