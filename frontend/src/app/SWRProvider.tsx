'use client';
import {ReactNode} from 'react';
import {SWRConfig} from 'swr';

type SWRProviderProps = {
  children: ReactNode;
};

export const SWRProvider = ({ children }: SWRProviderProps) => {

  const fetcher = async (url: string) => {

    const res = await fetch('/api/apim' + url)

    if (!res.ok) {
      throw await res.json()
    }

    return res.json()
  }

  return (
    <SWRConfig
      value={{
        onErrorRetry: (error, key, config, revalidate, { retryCount }) => {
          // Never retry on 404.
          if (error.status === 404) return;

          // Never retry for a specific key.
          // if (key === '/api/user') return

          // Only retry up to 3 times.
          if (retryCount >= 3) return;

          // Retry after 5 seconds.
          setTimeout(() => revalidate({ retryCount }), 5000);
        },
        fetcher: (url) => fetcher(url),
      }}
    >
      {children}
    </SWRConfig>
  );
};
