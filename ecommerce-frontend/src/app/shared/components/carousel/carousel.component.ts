import { Component, Input, OnInit, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { Banner } from '../../../core/services/banner.service';

@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.css']
})
export class CarouselComponent implements OnInit, OnDestroy, OnChanges {
  @Input() banners: Banner[] = [];
  index = 0;
  interval: any;

  ngOnInit() { }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['banners'] && this.banners?.length > 0) {
      this.startAutoSlide();
    }
  }

  ngOnDestroy() {
    this.stopAutoSlide();
  }

  startAutoSlide() {
    this.stopAutoSlide();
    this.interval = setInterval(() => this.next(), 5000);
  }

  stopAutoSlide() {
    if (this.interval) {
      clearInterval(this.interval);
      this.interval = null;
    }
  }

  next() {
    if (this.banners.length > 0) {
      this.index = (this.index + 1) % this.banners.length;
    }
  }

  prev() {
    if (this.banners.length > 0) {
      this.index = (this.index - 1 + this.banners.length) % this.banners.length;
    }
  }
}
