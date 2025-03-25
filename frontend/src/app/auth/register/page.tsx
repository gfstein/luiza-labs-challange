'use client'

import {Card, InputAdornment, TextField} from "@mui/material";
import Typography from "@mui/material/Typography";
import {AccountCircleRounded, Visibility, VisibilityOff} from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";
import Button from "@mui/material/Button";
import {toast} from "react-toastify";
import {ResponseError} from "@/types/response-error";
import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {useState} from "react";
import {RegisterRequest} from "@/types/register-request";
import {apim} from "@/lib/apim.service";
import {useRouter} from "next/navigation";

export default function RegisterPage() {

  const [showPassword, setShowPassword] = useState(false);
  const router = useRouter();

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const schema = z.object({
    name: z.string().min(2, 'Nome deve ter pelo menos 2 caracteres'),
    email: z.string().email("E-mail inválido"),
    password: z.string()
      .min(6, "A senha deve ter pelo menos 6 caracteres")
      .regex(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&+=!]).{6,}$/,
        "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um dígito e um caractere especial."),
    confirmPassword: z.string()
  }).refine((data) => data.password === data.confirmPassword, {
    message: "As senhas não conferem",
    path: ["confirmPassword"]
  });

  const {register, handleSubmit, formState: {errors}} = useForm<RegisterRequest>({
    resolver: zodResolver(schema)
  });

  const onSubmit = async (formData: RegisterRequest) => {
    try {
      await apim.post('/auth/signup', formData)
      toast.success('Usuário cadastrado com sucesso!')
      router.push('/auth/signin')
    } catch (error) {
      toast.error((error as ResponseError).message);
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex justify-center mt-56">
      <Card className={'p-6 max-w-md'} variant="outlined">
        <Typography align={'center'} variant="h4">
          Registre-se
        </Typography>
        <br/>

        <TextField fullWidth label='Nome' {...register('name')} error={!!errors.name}
                   helperText={errors.name?.message as string || " "}
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
        <TextField type={showPassword ? 'text' : 'password'} fullWidth label='Senha' {...register('password')}
                   error={!!errors.password}
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

        <TextField type={showPassword ? 'text' : 'password'} fullWidth label='Confirmar Senha' {...register('confirmPassword')}
                   error={!!errors.confirmPassword}
                   helperText={errors.confirmPassword?.message as string || " "}
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
        <Button type="submit" fullWidth variant="contained" size="large">Enviar</Button>
      </Card>
    </form>
  )
}