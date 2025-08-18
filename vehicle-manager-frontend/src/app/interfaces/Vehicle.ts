import { Model } from "./Model";

export interface Vehicle {
    id?: number;
    model: Model,
    year: number,
    plate: String,
    vehicleType: 'CARRO' | 'MOTO' | 'CAMINHAO' | 'ONIBUS';
}