import React, { useState } from 'react';
import { Plus, Upload, User, History, Home } from 'lucide-react';
import { useRouter } from 'next/navigation';

const FloatingActionButton = () => {
    const router = useRouter();

    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div className="relative z-50">
            <div className="absolute w-16 h-16 rounded-full right-12 bottom-12 shadow-md "></div>

            <div
                className={`absolute right-12 bottom-12 bg-transparent w-16 overflow-hidden transition-all duration-300 ease-in-out rounded-full ${isOpen ? 'h-80' : 'h-16'}`}
                onMouseEnter={() => setIsOpen(true)}
                onMouseLeave={() => setIsOpen(false)}
            >
                <div className="flex flex-col items-center space-y-2 pt-2 pb-20 ">
                    <button
                        className="w-12 h-12 rounded-full bg-purple-600 shadow hover:shadow-md transition-shadow flex items-center justify-center "
                        onClick={() => {
                            router.push('/upload');
                        }}
                    >
                        <Upload className="w-6 h-6 text-white" />
                    </button>

                    <button
                        className="w-12 h-12 rounded-full bg-red-500 shadow hover:shadow-md transition-shadow flex items-center justify-center"
                        onClick={() => {
                            router.push('/mypage');
                        }}
                    >
                        <User className="w-6 h-6 text-white" />
                    </button>

                    <button className="w-12 h-12 rounded-full bg-yellow-500 shadow hover:shadow-md transition-shadow flex items-center justify-center">
                        <History
                            className="w-6 h-6 text-white"
                            onClick={() => {
                                router.push('/history');
                            }}
                        />
                    </button>

                    <button className="w-12 h-12 rounded-full bg-blue-500 shadow hover:shadow-md transition-shadow flex items-center justify-center">
                        <Home
                            className="w-6 h-6 text-white"
                            onClick={() => {
                                router.push('/');
                            }}
                        />
                    </button>
                </div>

                <button
                    onClick={toggleMenu}
                    className="absolute bottom-0 right-0 w-16 h-16 rounded-full bg-primary z-10 flex items-center justify-center transition-transform duration-300 shadow-lg "
                    style={{
                        transform: isOpen ? 'rotate(180deg)' : 'rotate(0)'
                    }}
                >
                    <Plus className="w-8 h-8 text-white" />
                </button>
            </div>
        </div>
    );
};

export default FloatingActionButton;
