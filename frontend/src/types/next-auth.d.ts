import {DefaultSession} from 'next-auth';

declare module 'next-auth' {
  interface Session {
    user: AccessProfile & DefaultSession['user'];
    accessToken: string;
    refreshToken: string;
  }

  interface User {
    email: string;
    accessToken: string;
    refreshToken: string;
  }
}
