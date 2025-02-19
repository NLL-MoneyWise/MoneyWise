const saveToekn = async (accessToken: string, refreshToken: string) => {
    try {
        await fetch('/api/auth/token', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                accessToken,
                refreshToken
            })
        });
    } catch (error) {
        console.error(error);

        throw new Error('토큰을 저장하는데 실패했습니다.');
    }
};

export default saveToekn;
