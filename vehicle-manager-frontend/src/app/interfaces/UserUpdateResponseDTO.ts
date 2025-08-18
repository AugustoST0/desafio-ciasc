import { User } from "./User";

export interface UserUpdateResponseDTO {
    user: User;
    accessToken: string;
    refreshToken: string;
}