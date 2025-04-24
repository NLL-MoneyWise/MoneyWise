import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UserState {
    user: {
        nickName: string;
        email: string;
    } | null;
    setUser: (user: UserState['user']) => void;
    isLoggedIn: () => boolean;
    claerUser: () => void;
    clearStorage: () => void;
}

export const useUserStore = create<UserState>()(
    persist(
        (set, get) => ({
            user: null,
            accessToken: null,
            setUser: (user) => set({ user }),
            isLoggedIn: () => {
                return get().user !== null;
            },
            clearStorage: () => {
                localStorage.removeItem('user-storage');
            },
            claerUser: () => {
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
