'use client';
import * as React from 'react';
import {useEffect} from 'react';
import {AppProvider} from '@toolpad/core/AppProvider';
import {type AuthProvider, SignInPage} from '@toolpad/core/SignInPage';
import {useTheme} from '@mui/material/styles';
import {signIn} from 'next-auth/react';
import {useSearchParams} from "next/navigation";
import {toast} from "react-toastify";

// preview-start
const providers = [{ id: 'credentials', name: 'Email and Password' }];
// preview-end

const login: (provider: AuthProvider, formData: FormData) => void = async (
  provider,
  formData,
) => {
  return await signIn('credentials',
    { email: formData.get('email'), password: formData.get('password'), redirect: true  });
};

export default function CredentialsSignInPage() {

  const params = useSearchParams();
  const error = params.get('error');

  useEffect(() => {
    if (error) {
      let errorMessage = '';

      if (error === 'OAuthAccountNotLinked') {
        errorMessage = 'Essa conta já está associada a um provedor diferente.';
      } else if (error === 'CredentialsSignin') {
        errorMessage = 'Login ou senha inválidos.';
      } else if (error === 'AccessDenied') {
        errorMessage = 'Acesso negado.';
      } else if (error === 'OAuthCallback') {
        errorMessage = 'Erro ao tentar acessar o provedor.';
      } else {
        errorMessage = `Erro desconhecido: ${error}`;
      }

      // Exibir o toast com a mensagem de erro
      toast.error(errorMessage);
    }
  }, [error]);

  const theme = useTheme();
  return (
    // preview-start
    <AppProvider theme={theme}>
      <SignInPage
        signIn={login}
        providers={providers}
        slotProps={{ emailField: { autoFocus: false } }}
      />
    </AppProvider>
    // preview-end
  );
}

