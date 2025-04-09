import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UserState {
    user: {
        nickName: string;
        email: string;
    } | null;

    setUser: (user: UserState['user']) => void;
    isLoggedIn: () => boolean;
    logout: () => void;
}

export const useUserStore = create<UserState>()(
    persist(
        (set, get) => ({
            user: null,
            setUser: (user) => set({ user }),
            isLoggedIn: () => {
                return get().user !== null;
            },
            logout: () => set({ user: null })
        }),
        {
            name: 'user-storage',
            partialize: (state) => ({
                user: state.user
            })
        }
    )
);
