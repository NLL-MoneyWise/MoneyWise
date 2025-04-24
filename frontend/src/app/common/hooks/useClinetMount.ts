import { useState, useEffect } from 'react';

const useClientMount = () => {
    const [isMounted, setIsMounted] = useState(false);

    useEffect(() => {
        setIsMounted(true);
    }, []);

    return { isMounted };
};

export default useClientMount;
