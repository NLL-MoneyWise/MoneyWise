//import { getAccessToken, removeAccessToken } from '@/app/auth/util/saveToekn';
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UserState {
    user: {
        nickName: string;
        email: string;
    } | null;
    setUser: (user: UserState['user']) => void;
    //isLoggedIn: () => boolean;
    logout: () => void;
    clearStorage: () => void;
}

export const useUserStore = create<UserState>()(
    persist(
        (set, get) => ({
            user: null,
            accessToken: null,
            setUser: (user) => set({ user }),
            // isLoggedIn: () => {
            //     return getAccessToken() !== null;
            // },
            clearStorage: () => {
                localStorage.removeItem('user-storage');
            },
            logout: () => {
                set({ user: null });
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
