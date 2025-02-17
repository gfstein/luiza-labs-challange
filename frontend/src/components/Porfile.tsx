'use client'

import useSWR from "swr";
import {Paper, Skeleton, TextField} from "@mui/material";
import {toast} from "react-toastify";
import {User} from "@/types/user";
import {useEffect} from "react";
import Button from "@mui/material/Button";
import {useForm} from "react-hook-form";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {apim} from "@/lib/apim.service";
import {ResponseError} from "@/types/response-error";

type UserDetails = {
    name: string,
    email: string
}

export default function Profile() {
  const {data, error, isLoading} = useSWR<User>('/users/me');

  const schema = z.object({
    name: z.string().min(3, "Nome deve ter pelo menos 3 caracteres"),
    email: z.string().email("E-mail inválido"),
  });

  const {register, handleSubmit, formState: {errors}, reset} = useForm<UserDetails>({
    resolver: zodResolver(schema)
  });

  const onSubmit = async (formData: UserDetails) => {
    try {
      await apim.patch('/users', formData);
      toast.success('Dados atualizados com sucesso');
    } catch (error) {
      toast.error((error as ResponseError).message);
    }
  }

  useEffect(() => {
    toast.error(error?.message);
  }, [error]);

  useEffect(() => {
    reset({
      name: data?.name,
      email: data?.email
    })
  }, [data, reset]);

  if (isLoading) return <Skeleton variant="rectangular" width={210} height={118}/>

  return (
    <Paper elevation={3} className='max-w-5xl mx-auto'>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className='p-4 flex flex-col items-center justify-between'>
          <h2>Atualizar dados pessoais</h2><br/>
          <div className='flex flex-col md:flex-row items-center justify-between gap-2 w-full'>
            <TextField fullWidth label='Nome' {...register('name')} error={!!errors.name} helperText={errors.name?.message as string || " "}/>
            <TextField fullWidth label='E-Mail' {...register('email')} error={!!errors.email} helperText={errors.email?.message as string || " "}/>
          </div>
          <br/>
          <Button type='submit' variant='contained' size='large' color='primary'>Atualizar</Button>
        </div>
      </form>
    </Paper>
  )

}