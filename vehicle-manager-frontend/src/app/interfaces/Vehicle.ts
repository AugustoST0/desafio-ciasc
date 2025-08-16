export interface Vehicle {
    id?: number;
    brand: String,
    model: String,
    year: number,
    plate: String,
    vehicleType: 'CARRO' | 'MOTO' | 'CAMINHAO' | 'ONIBUS';
}