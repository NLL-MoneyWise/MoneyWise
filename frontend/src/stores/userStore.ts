import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UserState {
    user: {
        nickName: string;
        email: string;
    } | null;
    accessToken: string | null;
    setAccessToken: (token: string) => void;
    setUser: (user: UserState['user']) => void;
    isLoggedIn: () => boolean;
    logout: () => void;
    clearStorage: () => void;
}

export const useUserStore = create<UserState>()(
    persist(
        (set, get) => ({
            user: null,
            accessToken: null,
            setUser: (user) => set({ user }),
            setAccessToken: (token: string) => set({ accessToken: token }),
            isLoggedIn: () => {
                return get().accessToken !== null;
            },
            clearStorage: () => {
                localStorage.removeItem('user-storage');
            },
            logout: () => {
                set({ user: null, accessToken: null });
                // 아래에서 추가한 clearStorage 함수 호출
                get().clearStorage();
            }
        }),
        {
            name: 'user-storage',
            partialize: (state) => ({
                user: state.user
            })
        }
    )
);
