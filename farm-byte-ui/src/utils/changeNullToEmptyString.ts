export const changeNullToEmptyString = (custom: object) => {
    // @ts-ignore
    return JSON.parse(JSON.stringify(custom), (key, value) => value === null ? '' : value)
}