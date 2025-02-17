import {NextRequest, NextResponse} from 'next/server';
import {getToken} from 'next-auth/jwt';
import {ResponseError} from '@/types/response-error';
import {NextURL} from "next/dist/server/web/next-url";

export async function GET(req: NextRequest) {
  try {
    const res = await fetch(getUri(req.nextUrl), await getRequestOptions(req, 'GET'));
    return NextResponse.json(await res.json(), {status: res.status});
  } catch (e) {
    return handleErrors(e as ResponseError);
  }
}

export async function PUT(req: NextRequest) {
  try {
    const res = await fetch(getUri(req.nextUrl), await getRequestOptions(req, 'PUT'));
    return NextResponse.json(await res.json(), {status: res.status});
  } catch (e) {
    return handleErrors(e as ResponseError);
  }
}

export async function PATCH(req: NextRequest) {
  try {
    const res = await fetch(getUri(req.nextUrl), await getRequestOptions(req, 'PATCH', await req.json()));
    return NextResponse.json(await res.json(), {status: res.status});
  } catch (e) {
    return handleErrors(e as ResponseError);
  }
}

export async function POST(req: NextRequest) {
  try {
    const res = await fetch(getUri(req.nextUrl), await getRequestOptions(req, 'POST'));
    return NextResponse.json(await res.json(), {status: res.status});
  } catch (e) {
    return handleErrors(e as ResponseError);
  }
}

export async function DELETE(req: NextRequest) {
  try {
    const res = await fetch(getUri(req.nextUrl), await getRequestOptions(req, 'DELETE'));
    return NextResponse.json(await res.json(), {status: res.status});
  } catch (e) {
    return handleErrors(e as ResponseError);
  }
}

const handleErrors = (error: ResponseError) => {
  return NextResponse.json(error, {status: error.status || 500});
}

const apimIndex = 9; // /api/apim = 9 letras
const getUri = (url: NextURL) => {
  return process.env.BACKEND_BASE_URL + url.pathname.substring(apimIndex, url.pathname.length)
}

type Method = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
const getRequestOptions = async (req: NextRequest, method: Method, body?: never) => {
  const token = await getToken({req, secret: process.env.JWT_SECRET});
  return {
    method,
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token?.accessToken}`,
    },
    body: body ? JSON.stringify(body) : undefined,
  }
}