'use client'

import {Geist, Geist_Mono} from "next/font/google";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import "./globals.css";
import {ReactNode} from "react";
import SessionWrapper from "@/app/SessionWrapper";
import {SWRProvider} from "@/app/SWRProvider";
import {ToastContainer} from "react-toastify";
import {CssBaseline} from "@mui/material";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export default function RootLayout({
                                     children,
                                   }: Readonly<{
  children: ReactNode;
}>) {
  return (
    <html lang="en">
    <body className={`${geistSans.variable} ${geistMono.variable} antialiased`}>
    <SessionWrapper>
      <SWRProvider>
        <CssBaseline/>
        <ToastContainer/>
        {children}
      </SWRProvider>
    </SessionWrapper>
    </body>
    </html>
  );
}
