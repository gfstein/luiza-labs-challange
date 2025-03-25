import React from 'react';
import {Box, Card, Divider, Skeleton} from "@mui/material";
import Grid from '@mui/material/Grid2';

interface ShowCaseSkeletonProps {
  itemCount?: number;
}

const ShowCaseSkeleton: React.FC<ShowCaseSkeletonProps> = ({ itemCount = 8 }) => {
  return (
    <div className="flex flex-wrap">
      <Grid container spacing={2}>
        {Array.from(new Array(itemCount)).map((_, index) => (
          <Grid key={index} size={{xs: 12, sm: 4, md: 3}} className="flex justify-center">
            <Card className='h-auto w-full p-6'>
              {/* Cabeçalho do card */}
              <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                <Skeleton variant="text" width={80} height={20} />
                <Skeleton variant="circular" width={32} height={32} />
              </Box>

              {/* Imagem do produto */}
              <Box sx={{ height: 200, width: '100%', mb: 2, display: 'flex', justifyContent: 'center' }}>
                <Skeleton variant="rectangular" width="80%" height="100%" />
              </Box>

              <Divider sx={{ width: '100%', my: 2 }} />

              {/* Título do produto */}
              <Box display="flex" justifyContent="center">
                <Skeleton variant="text" width="90%" height={28} />
              </Box>

              {/* Avaliação com estrelas */}
              <Box display="flex" justifyContent="center" sx={{ my: 1 }}>
                <Skeleton variant="rectangular" width={120} height={20} sx={{ borderRadius: 1 }} />
              </Box>

              {/* Número de avaliações */}
              <Box display="flex" justifyContent="center">
                <Skeleton variant="text" width={60} height={16} />
              </Box>

              {/* Preço */}
              <Box display="flex" justifyContent="center" sx={{ mt: 2 }}>
                <Skeleton variant="rectangular" width={80} height={32} sx={{ borderRadius: 1 }} />
              </Box>
            </Card>
          </Grid>
        ))}
      </Grid>
    </div>
  );
};

export default ShowCaseSkeleton;