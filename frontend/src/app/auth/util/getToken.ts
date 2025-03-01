import { TokenType } from './../types/token/index';

export const getToken = async (type: TokenType) => {
    try {
        const response = await fetch(`/api/auth/token?type=${type}`, {
            method: 'GET'
        });

        const { Token } = await response.json();
        return Token;
    } catch (error) {
        console.error(error);

        throw new Error('토큰을 가져오는데 실패했습니다.');
    }
};
