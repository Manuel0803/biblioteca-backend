package com.biblioteca.model.entity;

import com.biblioteca.model.enums.EstadoLibro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Long idLibro;

    @NotBlank(message = "El título es obligatorio")
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Column(name = "autor", nullable = false, length = 255)
    private String autor;

    @NotBlank(message = "El ISBN es obligatorio")
    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoLibro estado;

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    public Libro() {
        this.estado = EstadoLibro.DISPONIBLE;
    }

    public Libro(String titulo, String autor, String isbn, Categoria categoria) {
        this();
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.categoria = categoria;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public EstadoLibro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean estaDisponible() {
        return this.estado == EstadoLibro.DISPONIBLE;
    }

    public void marcarComoPrestado() {
        if (!estaDisponible()) {
            throw new IllegalStateException("El libro no está disponible para préstamo");
        }
        this.estado = EstadoLibro.PRESTADO;
    }

    public void marcarComoDisponible() {
        this.estado = EstadoLibro.DISPONIBLE;
    }
}