import { useState, useEffect } from 'react';

const useClientMount = () => {
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        setMounted(true);
    }, []);

    return mounted;
};

export default useClientMount;
