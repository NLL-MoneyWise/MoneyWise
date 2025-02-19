export class CustomError extends Error {
    constructor(
        message: string,
        public status: number,
        public type: ErrorType
    ) {
        super(message);
        this.name = 'CustomError';
    }
}

export enum ErrorType {
    API = 'API_ERROR',
    AUTH = 'AUTH_ERROR',
    VALIDATION = 'VALIDATION_ERROR',
    RENDER = 'RENDER_ERROR',
    NETWORK = 'NETWORK_ERROR',
    UNKNOWN = 'UNKNOWN_ERROR'
}
