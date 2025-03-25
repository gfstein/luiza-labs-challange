type Product = {
  id: number;
  title: string;
  price: number;
  description: string;
  category: string;
  image: string;
  favorited: boolean;
  rating: {
    rate: number;
    count: number;
  };
};