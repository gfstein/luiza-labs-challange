import {Container} from "@mui/material";

export default function AuthLayout({children}: { children: React.ReactNode }) {
    return (
        <Container maxWidth="xl">
            {children}
        </Container>
    );
}