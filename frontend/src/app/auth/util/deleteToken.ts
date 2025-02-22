export const deleteToken = async () => {
    try {
        await fetch('/api/auth/token', {
            method: 'DELETE'
        });
    } catch (error) {
        console.error(error);
        throw new Error('토큰을 삭제하는데 실패했습니다.');
    }
};
