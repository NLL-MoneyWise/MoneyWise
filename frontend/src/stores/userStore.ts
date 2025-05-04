import { getAccessToken } from '@/app/auth/util/toekn';
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UserState {
    user: {
        nickName: string;
        email: string;
    } | null;
    setUser: (user: UserState['user']) => void;
    isLoggedIn: () => boolean;
    clearUser: () => void;
    clearStorage: () => void;
}

export const useUserStore = create<UserState>()(
    persist(
        (set, get) => ({
            user: null,
            accessToken: null,
            setUser: (user) => set({ user }),
            isLoggedIn: () => {
                if (get().user) {
                    return true;
                } else {
                    return false;
                }
            },
            clearStorage: () => {
                localStorage.removeItem('user-storage');
            },
            clearUser: () => {
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
