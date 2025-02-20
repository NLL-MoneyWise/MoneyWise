/**
 *
 * @param callback 디바운싱 대상이 될 함수를 넣어준다.
 * @param delay 딜레이 될 시간을 넣어준다.
 * @returns
 */
const debounce = <T extends unknown[]>(
    callback: (...args: T) => void,
    delay: number
) => {
    let timer: NodeJS.Timeout;

    return (...args: T) => {
        clearTimeout(timer);
        timer = setTimeout(() => {
            callback(...args);
        }, delay);
    };
};
export default debounce;
