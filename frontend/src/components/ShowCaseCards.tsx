import * as React from 'react';
import {Card, CardMedia, Divider, IconButton, Rating} from "@mui/material";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import Grid from "@mui/material/Grid2";

type ShowCaseCardsProps = {
  products: Product[];
  loading: boolean;
  handleFavorite: (item: Product) => void;
};

export const ShowCaseCards = ({products, loading, handleFavorite}: ShowCaseCardsProps) => {
  return (
    <Grid container spacing={2}>
      {products.map(product => (
        <Grid key={product.id} size={{xs: 12, sm: 4, md: 2}} className="flex justify-center">
          <Card className='h-auto w-full p-6'>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
              <Typography variant="subtitle2" color="text.secondary">
                {product.category}
              </Typography>
              <IconButton loading={loading} disabled={loading} onClick={() => handleFavorite(product)}>
                {product.favorited ? <FavoriteIcon/> : <FavoriteBorderIcon/>}
              </IconButton>
            </Box>

            <div className='flex flex-col items-center justify-center'>
              <Box sx={{height: 200, width: '100%', position: 'relative', mb: 2}}>
                <CardMedia
                  component="img"
                  image={product.image}
                  title={product.title}
                  sx={{
                    height: '100%',
                    objectFit: 'contain'
                  }}
                />
              </Box>

              <Divider sx={{width: '100%', my: 2}}/>

              <Typography variant="h6" sx={{textAlign: 'center', mb: 1}}>
                {product.title}
              </Typography>

              <Rating value={product.rating?.rate || 0}/>

              <Typography variant="caption" color="text.secondary">
                {product.rating?.count || 0} avaliações
              </Typography>

              <Typography variant="h6" color="primary" sx={{mt: 2}}>
                R$ {product.price.toFixed(2)}
              </Typography>
            </div>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
};