import { Component, OnInit } from '@angular/core';
import { ModalService } from '../../services/modal-service';
import { ModalOptions } from '../../interfaces/ModalOptions';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal-component',
  imports: [CommonModule],
  templateUrl: './modal-component.html',
  styleUrl: './modal-component.css',
})
export class ModalComponent implements OnInit {
  isVisible = false;
  options?: ModalOptions;

  constructor(private modalService: ModalService) {}

  ngOnInit() {
    this.modalService.modal$.subscribe((options) => {
      if (options) {
        this.options = options;
        this.isVisible = true;
      } else {
        // Fechar modal quando receber null
        this.isVisible = false;
        this.options = undefined;
      }
    });
  }

  close() {
    this.isVisible = false;
    this.modalService.close();
  }

  confirm() {
    this.isVisible = false;
    this.modalService.confirm();
  }

  cancel() {
    this.isVisible = false;
    this.modalService.cancel();
  }
}
