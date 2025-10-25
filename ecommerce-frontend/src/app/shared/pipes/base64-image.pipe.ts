import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'base64Image'
})
export class Base64ImagePipe implements PipeTransform {
    transform(value: string | null, mime: string = 'image/jpeg'): string {
        if (!value) {
            return 'assets/no-image.png';
        }

        if (value.startsWith('data:image')) {
            return value;
        }

        return `data:${mime};base64,${value}`;
    }
}
