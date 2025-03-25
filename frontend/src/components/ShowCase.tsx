'use client'

import React, {useEffect, useState} from 'react';
import useSWR from "swr";
import {Divider} from "@mui/material";
import Typography from "@mui/material/Typography";
import {ProductFavorite} from "@/types/product-favorite";
import {apim} from "@/lib/apim.service";
import ShowCaseSkeleton from "@/components/ShowCaseSkeleton";
import {ShowCaseCards} from "@/components/ShowCaseCards";
import {toast} from "react-toastify";
import {ResponseError} from "@/types/response-error";
import Button from "@mui/material/Button";

const ShowCase = () => {

  const [loading, setLoading] = React.useState(false);
  const [products, setProducts] = useState<Product[]>([]);
  const [favorites, setFavorites] = useState<Product[]>([])
  const [apiProd, setApiProd] = useState<Product[]>([])

  const {data: apiData, isLoading, error} = useSWR<Product[]>('https://fakestoreapi.com/products');
  const {data: favData, isLoading: loadingFav, error: errorFav} = useSWR<ProductFavorite[]>('/users/favorite-list');

  useEffect(() => {
    if (apiData && favData) {
      setApiProd(apiData)
      setFavorites(apiData.filter(product => favData.some(fav => fav.id === product.id))
        .map(value => ({...value, favorited: true})))
      setProducts(apiData.filter(product => !favData.some(fav => fav.id === product.id)))
    }
  }, [apiData, favData]);

  const handleFavorite = async (item: Product) => {
    try {
      setLoading(true);
      const prod: ProductFavorite = {id: item.id, title: item.title, description: item.description}

      const find = favorites.find(fav => fav.id === item.id);
      if (find) {
        await apim.delete(`/users/favorite/${find.id}`)
        setFavorites(prevFavorites =>
          prevFavorites.filter(favorite => favorite.id !== find.id)
        );
        products.push({...item, favorited: false})
      } else {
        await apim.post('/users/favorite', prod)
        favorites.push({...item, favorited: true})
        products.splice(products.indexOf(item), 1)
      }
    } catch (e) {
      console.error(e)
      toast.error((e as ResponseError).message);
    } finally {
      setLoading(false);
    }
  }

  const removeAllFavorites = async () => {
    try {
      setLoading(true);
      await apim.delete('/users/favorite-list')
      setFavorites([])
      setProducts(apiProd)
    } catch (e) {
      console.error(e)
      toast.error((e as ResponseError).message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex flex-col">
      <div className="flex flex-row justify-between items-center">
        <Typography variant="h4">Favoritos</Typography>
        <Button variant={'contained'} onClick={removeAllFavorites} disabled={favorites.length == 0} >Remover Todos</Button>
      </div>
      <br/>

      {loadingFav ? <ShowCaseSkeleton itemCount={4}/>
        : errorFav ? <div>Failed to load</div>
          : favorites.length === 0 ? <div className='min-h-96' >Nenhum favorito</div>
            : <ShowCaseCards products={favorites} loading={loading} handleFavorite={handleFavorite}/>
      }

      <Divider sx={{width: '100%', my: 2}}/>

      <Typography variant="h4">Loja</Typography><br/>

      {isLoading ? <ShowCaseSkeleton/>
        : error ? <div>Failed to load</div>
          : products.length === 0 ? <div>Nenhum produto</div>
            : <ShowCaseCards products={products} loading={loading} handleFavorite={handleFavorite}/>
      }

    </div>
  );
};

export default ShowCase;
