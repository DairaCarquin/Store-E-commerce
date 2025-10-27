import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'base64Image' })
export class Base64ImagePipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value || value.trim() === '') return 'assets/no-image.png';
    if (value.startsWith('data:image')) return value;
    const mime = value.charAt(0) === 'i' ? 'image/png' : 'image/jpeg';
    return `data:${mime};base64,${value}`;
  }
}
