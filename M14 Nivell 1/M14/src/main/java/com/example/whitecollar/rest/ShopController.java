package com.example.whitecollar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.whitecollar.models.Picture;
import com.example.whitecollar.models.Shop;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class ShopController {
	private final ShopRepository shopRepository;
	private final PictureRepository pictureRepository;

	@Autowired
	public ShopController(ShopRepository shopRepository, PictureRepository pictureRepository) {
		this.shopRepository = shopRepository;
		this.pictureRepository = pictureRepository;
	}
	// ----------------------------------------------
	// Crear Botiga
	// ----------------------------------------------
	// shopRepository.save,create a list of new child entities(picture) when
	// creating a new parent(library)
	// Postman: Post-localhost:8080/api/v1/shops/POST/shops/

	@PostMapping("/POST/shops/")
	public ResponseEntity<Shop> create(@Valid @RequestBody Shop shop) {
		Shop savedShop = shopRepository.save(shop);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedShop.getId())
				.toUri();

		return ResponseEntity.created(location).body(savedShop);
	}

	// Shop Update By id
	// Create a list of new child entities(picture) when updating an existing
	// parent(library) parent
	// via shopRepository.save(library);
	// Postman: Put-localhost:8080/api/v1/shops/1/

	@PutMapping("/{id}")
	public ResponseEntity<Shop> update(@PathVariable Integer id, @Valid @RequestBody Shop shop) {
		/*
		 * Optional.A container object which may or may not contain a non-null value.If
		 * a value is present, isPresent() returns true. If novalue is present, the
		 * object is considered empty and isPresent() returns false.
		 */
		Optional<Shop> optionalShop = shopRepository.findById(id);
		if (!optionalShop.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		shop.setId(optionalShop.get().getId());
		shopRepository.save(shop);

		return ResponseEntity.noContent().build();
	}

	// Delete a library by ID and all books belong to it
	// via shopRepository.delete(optionalShop.get()); in the Delete API
	// Postman: Delete-localhost:8080/api/v1/shops/1/

	@DeleteMapping("/{id}")
	public ResponseEntity<Shop> delete(@PathVariable Integer id) {
		Optional<Shop> optionalShop = shopRepository.findById(id);
		if (!optionalShop.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		deleteShopInTransaction(optionalShop.get());

		return ResponseEntity.noContent().build();
	}

	@Transactional
	public void deleteShopInTransaction(Shop shop) {
		pictureRepository.deleteByShopId(shop.getId());
		shopRepository.delete(shop);
	}

	// Retrieve a list of child entities(picture) when retrieving a parent via
	// shopRepository.findById(id)
	// in the Get Shop API
	// Postman: Get-localhost:8080/api/v1/shops/1/

	@GetMapping("/{id}")
	public ResponseEntity<Shop> getById(@PathVariable Integer id) {
		Optional<Shop> optionalShop = shopRepository.findById(id);
		if (!optionalShop.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		return ResponseEntity.ok(optionalShop.get());
	}
	// ----------------------------------------------
	// Llistar botigues
	// ----------------------------------------------
	// Postman: Get- localhost:8080/api/v1/shops/GET/shops/

	@GetMapping("/GET/shops/")
	public ResponseEntity<Page<Shop>> getAll(Pageable pageable) {
		return ResponseEntity.ok(shopRepository.findAll(pageable));
	}

	// ----------------------------------------------
	// Llistar els quadres de la botiga
	// ----------------------------------------------
	// Retrieve a list of child entities(picture) when retrieving a parent via
	// pictureRepository.findByShopId(shopId, pageable));
	// in the Get Shop API
	// Postman: Get-localhost:8080/api/v1/GET/shops/{shopId}/pictures
	@GetMapping("/GET/shops/{shopId}/pictures")
	public ResponseEntity<Page<Picture>> getByShopId(@PathVariable Integer shopId, Pageable pageable) {
		return ResponseEntity.ok(pictureRepository.findByShopId(shopId, pageable));
	}

	// ----------------------------------------------
	// Afegir quadre
	// ----------------------------------------------
	// pictureRepository.save(picture),create a new picture
	// Postman: Post-localhost:8080/api/v1/POST/shops/{shopId}/pictures
	@PostMapping("/POST/shops/{shopId}/pictures")
	public ResponseEntity<Picture> create(@RequestBody @Valid Picture picture, @PathVariable Integer shopId) {
		Optional<Shop> optionalShop = shopRepository.findById(shopId);
		if (!optionalShop.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		picture.setShop(optionalShop.get());

		Picture savedPicture = pictureRepository.save(picture);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedPicture.getId()).toUri();

		return ResponseEntity.created(location).body(savedPicture);
	}

	// ----------------------------------------------
	// Incendiar els quadres de la botiga
	// ----------------------------------------------
	// Delete a library by ID and all books belong to it
	// via shopRepository.delete(optionalShop.get()); in the Delete API
	// Postman: Delete-localhost:8080/api/v1/shops/1/

	@DeleteMapping("/DELETE/shops/{id}/pictures")
	public ResponseEntity<Shop> incendiarQuadres(@PathVariable Integer id) {
		return delete(id);
	}

}