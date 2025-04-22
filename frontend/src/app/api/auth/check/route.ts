import { cookies } from 'next/headers';
import { NextResponse } from 'next/server';

export async function GET() {
    const cookieStore = await cookies();
    const token = cookieStore.get('accessToken');
    return NextResponse.json(
        { isLoggedIn: !!token },
        {
            headers: {
                'Cache-Control': 'max-age=5, s-maxage=5'
            }
        }
    );
}
