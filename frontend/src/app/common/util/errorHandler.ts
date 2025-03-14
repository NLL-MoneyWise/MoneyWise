import { CustomError, ErrorType } from '../types/error/error';

type ToastFunction = (
    message: string,
    variant: 'success' | 'warning' | 'error'
) => void;

export const handleCustomError = (error: unknown, addToast: ToastFunction) => {
    if (!(error instanceof CustomError)) {
        addToast('예상치 못한 오류가 발생했습니다', 'error');
        return;
    }

    switch (error.type) {
        case ErrorType.API:
            addToast(`API 오류: ${error.message}`, 'error');
            break;
        case ErrorType.AUTH:
            addToast('인증이 필요합니다', 'error');
            break;
        case ErrorType.VALIDATION:
            addToast(`${error.message}`, 'error');
            break;
        case ErrorType.NETWORK:
            addToast('네트워크 연결을 확인해주세요', 'error');
            break;
        case ErrorType.RENDER:
            addToast('화면 표시 중 오류가 발생했습니다', 'error');
            break;
        default:
            addToast('알 수 없는 오류가 발생했습니다', 'error');
    }
};
