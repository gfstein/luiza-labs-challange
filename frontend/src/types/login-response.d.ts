import {UUID} from "node:crypto";

export type LoginResponse = {
    id: UUID;
    email: string;
    accessToken: string;
    refreshToken: string;
}