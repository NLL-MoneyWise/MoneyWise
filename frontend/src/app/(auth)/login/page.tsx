'use client';

import LoginForm from '@/app/auth/components/LoginForm/LoginForm';
import Logo from '@/app/common/components/Logo/Logo';

export default function LoginPage() {
    return (
        <>
            <div className="flex justify-center mt-[40%] mb-10">
                <Logo variant="dark" width={320} height={41.7} />
            </div>
            <LoginForm />
        </>
    );
}
