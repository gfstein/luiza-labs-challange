import AppBar from "@/components/AppBar";
import {Container} from "@mui/material";

export default function PrivateLayout({ children }: { children: React.ReactNode }) {
  return (
    <div>
      <AppBar/><br/>
      <Container maxWidth="xl">
        {children}
      </Container>
    </div>
  );
}