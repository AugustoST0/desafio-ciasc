import { Brand } from "./Brand";
import { Model } from "./Model";

export interface Vehicle {
    id?: number;
    brand: Brand,
    model: Model,
    year: number,
    plate: String,
    vehicleType: 'CARRO' | 'MOTO' | 'CAMINHAO' | 'ONIBUS';
}