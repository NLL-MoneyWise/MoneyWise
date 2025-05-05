export const getWeekOfMonth = (date: Date) => {
    const firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1);
    const dayOfWeek = firstDayOfMonth.getDay();

    const dayOfMonth = date.getDate();

    return Math.ceil((dayOfMonth + dayOfWeek) / 7);
};
