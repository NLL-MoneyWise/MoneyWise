export interface MemoRequest {
    date: string;
    content: string;
}

export interface PutMemoRequest {
    id: number;
    memo: MemoRequest;
}

export interface DeleteMemoRequest {
    date: string;
}
