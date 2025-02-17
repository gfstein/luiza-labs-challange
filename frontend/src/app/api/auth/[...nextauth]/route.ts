import NextAuth, {NextAuthOptions} from 'next-auth';
import CredentialsProvider from 'next-auth/providers/credentials';
import {LoginResponse} from "@/types/login-response";
import {jwtDecode} from "jwt-decode";
import {JWT} from "next-auth/jwt";

export const authOptions: NextAuthOptions = {
  providers: [
    CredentialsProvider({
      name: 'Credentials',
      credentials: {
        email: {placeholder: 'E-mail', type: 'text'},
        password: {placeholder: 'Senha', type: 'password'},
      },
      async authorize(credentials) {
        const res = await fetch(process.env.BACKEND_BASE_URL + '/auth/login', {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify(credentials),
        });
        if (!res.ok) return null;

        const user: LoginResponse = await res.json();

        if (user.email) {
          return user;
        } else {
          return null;
        }
      },
    }),
  ],
  session: {
    strategy: 'jwt',
  },
  pages: {
    signIn: "/auth/signin",
    error: "/auth/signin",
  },
  callbacks: {
    async jwt({token, user}) {
      if (user) {
        token.accessToken = user.accessToken;
        token.refreshToken = user.refreshToken;
      }

      if (token && token.accessToken) {
        await verifyToken(token);
      }

      return token;
    },
  },
};

const verifyToken = async (token: JWT) => {
  const decodedToken = jwtDecode(token.accessToken as string);
  if (decodedToken.exp! * 1000 < Date.now()) {
    // Refresh token
    const res = await fetch(process.env.BACKEND_BASE_URL + '/auth/refresh', {
      method: 'POST',
      headers: {
        'Content-Type': 'text/plain'
      },
      body: token.refreshToken as string,
    });
    if (!res.ok) {
      throw new Error("Failed to refresh token");
    }

    const user: LoginResponse = await res.json();

    token.accessToken = user.accessToken;
    token.refreshToken = user.refreshToken;
  }
}

const handler = NextAuth(authOptions);

export {handler as GET, handler as POST};