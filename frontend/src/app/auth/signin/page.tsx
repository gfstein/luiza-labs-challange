'use client'

import {useSearchParams} from "next/navigation";
import {useEffect, useState} from "react";
import {toast} from "react-toastify";
import {Card, Checkbox, FormControlLabel, InputAdornment, TextField} from "@mui/material";
import Typography from "@mui/material/Typography";
import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {ResponseError} from "@/types/response-error";
import {LoginRequest} from "@/types/login-request";
import {signIn} from "next-auth/react";
import Button from "@mui/material/Button";
import Link from "next/link";
import {AccountCircleRounded, Visibility, VisibilityOff} from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";

export default function SigningPage() {

  const [showPassword, setShowPassword] = useState(false);
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

  const schema = z.object({
    email: z.string().email("E-mail inválido"),
    password: z.string()
      .min(6, "A senha deve ter pelo menos 6 caracteres")
      .regex(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&+=!]).{6,}$/,
        "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um dígito e um caractere especial."),
    rememberMe: z.boolean().optional()
  });

  const {register, handleSubmit, formState: {errors}} = useForm<LoginRequest>({
    resolver: zodResolver(schema)
  });

  const onSubmit = async (formData: LoginRequest) => {
    try {
      await signIn('credentials', {email: formData.email, password: formData.password, redirect: true})
    } catch (error) {
      toast.error((error as ResponseError).message);
    }
  }

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex justify-center mt-56">
      <Card className={'p-6 max-w-md'} variant="outlined">
        <Typography align={'center'} variant="h4">
          Login
        </Typography>
        <br/>

        <TextField fullWidth label='E-Mail' {...register('email')} error={!!errors.email}
                   helperText={errors.email?.message as string || " "}
                   slotProps={{
                     input: {
                       endAdornment: (
                         <InputAdornment position={'end'}>
                           <AccountCircleRounded/>
                         </InputAdornment>
                       )
                     }
                   }}
        />
        <TextField type={showPassword ? 'text' : 'password'} fullWidth label='Senha' {...register('password')} error={!!errors.password}
                   helperText={errors.password?.message as string || " "}
                   slotProps={{
                     input: {
                       endAdornment: (
                         <InputAdornment position="end">
                           <IconButton
                             onClick={handleClickShowPassword}
                             edge='end'
                           >
                             {showPassword ? <VisibilityOff sx={{mr: 0.5}}/> : <Visibility sx={{mr: 0.5}}/>}
                           </IconButton>
                         </InputAdornment>
                       )
                     }
                   }}
        />

        <div className='flex justify-between'>
          <FormControlLabel
            sx={{pt: 0}}
            control={<Checkbox sx={{pt: 0, pb: 0}} value="remember" color="primary"/>}
            label="Remember me"
          />
          <Link className='text-blue-700' href="/auth/forgot-password">Esqueceu a senha?</Link>
        </div>
        <br/>

        <Button type="submit" fullWidth variant="contained" size="large">Enviar</Button>
        <br/><br/>
        <Link className='text-blue-700' href="/auth/register">Não tem login? Cadastre-se</Link>
      </Card>
    </form>
  );
}

