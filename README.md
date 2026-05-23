# open-api-services
Payment integration with PayPal and Mercado Pago
Backend API developed with Spring Boot for e-commerce and payment processing.

---

## ⚙️ Tech Stack

- Java 17+
- Spring Boot
- JPA / Hibernate
- posgresSQl
- Mercado Pago API
- WebSockets (real-time notifications)
- DTO Pattern
- API Response Wrapper

---

## 💳 Features

- Payment integration with Paypal
- Payment integration with Mercado Pago
- Webhook payment confirmation
- Real-time order updates via WebSocket
- Global exception handling
- HMAC signature security interceptor

---
## 🔐 Security

- Signature validation via HMAC SHA-256
- External reference id for idempotency

# 💳 Payment Controller - API Overview

This controller manages all payment operations for PayPal and Mercado Pago, including payment creation, refunds, and webhook processing.

## Controller Code

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final IPayPalService payPalService;
    private final IMercadoPagoService mercadoPagoService;
    private final ICheckoutService checkoutService;

    public PaymentController(IPayPalService payPal,
                             IMercadoPagoService mercadoPago,
                             ICheckoutService checkout ) {
        this.payPalService = payPal;
        this.mercadoPagoService = mercadoPago;
        this.checkoutService = checkout;
    }

    @PostMapping("/payment-pp")
    public ApiResponse<String> processPayPalPayment(@RequestBody PayPalPaymentDTO dto) {
        return payPalService.createSale(dto);
    }

    @PostMapping("/payment-pp/refund/{paypalId}")
    public ApiResponse<String> refundPayPalPayment(@PathVariable String paypalId) {
        return payPalService.refundSale(paypalId);
    }

    @PostMapping("/payment-mp-create-preference")
    public ApiResponse<String> createMercadoPagoPreference(
            @RequestBody MercadoPagoPreferenceDTO dto) {

        return checkoutService.createPreference(dto);
    }

    @PostMapping("/payment-webhook")
    public ApiResponse<String> processMercadoPagoPayment(@RequestBody WebhookDTO mpDto) {
        return mercadoPagoService.createSale(mpDto);
    }

    @PostMapping("/payment-mc/refund/{mpId}")
    public ApiResponse<String> refundMercadoPagoPayment(@PathVariable String mpId) {
        return mercadoPagoService.refundSale(mpId);
    }
}

---

## 🧠 Controller Purpose

This controller acts as the central gateway for all payment-related operations. It integrates PayPal and Mercado Pago services, handles payment creation, refunds, and payment confirmations via webhooks.

---

## 📌 Endpoints Explanation

### 💳 PayPal Payment

POST /api/payments/payment-pp

Creates a PayPal payment record after the user completes checkout.

**Objective:**
- Register successful PayPal transactions
- Store payer and item details
- Confirm completed payments in the system

---

### 💰 PayPal Refund

POST /api/payments/payment-pp/refund/{paypalId}

Refunds a previously completed PayPal transaction.

**Objective:**
- Reverse PayPal payments
- Restore order state if needed
- Handle customer refund requests

---

### 🏦 Mercado Pago Preference Creation

POST /api/payments/payment-mp-create-preference

Creates a Mercado Pago checkout preference used by the frontend to start the payment process.

**Objective:**
- Generate payment preference ID
- Link order with externalReference
- Send checkout configuration to frontend

---

### 🔔 Mercado Pago Webhook

POST /api/payments/payment-webhook

Receives real-time payment notifications from Mercado Pago.

**Objective:**
- Validate payment status
- Confirm successful transactions
- Update internal order state
- Trigger business logic (stock, notifications, etc.)

---

### 💸 Mercado Pago Refund

POST /api/payments/payment-mc/refund/{mpId}

Refunds a Mercado Pago transaction.

**Objective:**
- Cancel or reverse approved payments
- Handle refund logic in backend
- Maintain financial consistency

---

## 🔄 Overall Flow

Frontend → Create Preference → User Payment → Webhook Confirmation → Order Update → Optional Refunds

---

## ⚠️ Notes

- PayPal and Mercado Pago are handled separately but unified in this controller
- Webhook is the ONLY trusted source for Mercado Pago payment confirmation
- Refund endpoints should be restricted and secured
- External references are used to link payments with internal orders


# 💳 PayPal Payment API

This endpoint processes PayPal payments and registers the sale in the system.

## Endpoint
POST /api/payments/payment-pp

## Request Body (PayPalPaymentDTO)

This endpoint receives PayPal payment confirmation data including payer info, items purchased, and transaction details.

```json
{
  "paypal_id": "12079938WL67974329",
  "payer": {
    "name": "John Doe",
    "email": "donejon@gmail.com",
    "country": "PE"
  },
  "items": [
    {
      "id": 1,
      "name": "zapatillas adidas talla 42",
      "price": 3,
      "quantity": 1,
      "subtotal": 3
    }
  ],
  "total": 3,
  "currency": "USD",
  "status": "COMPLETED",
  "date": "2026-05-21T04:03:17.279Z"
}
```

## Response
```json
{
"status": true,
"message": "PayPal sale created successfully",
"data": "12079938WL67974329"
}
```
## Description

This endpoint:

- Receives confirmed PayPal payment data
- Validates transaction status (COMPLETED)
- Registers sale in the system
- Stores payer and items information
- Links transaction using paypal_id
- Returns PayPal transaction reference ID

## Key Fields

- paypal_id → Unique PayPal transaction ID
- payer.email → Customer email
- items → Purchased products list
- total → Total amount paid
- status → Payment status (must be COMPLETED)
- currency → Payment currency (USD, etc.)

## Flow

Frontend → PayPal Checkout → Payment Approved → Backend API → Sale Registered → Response Returned

## Notes

- Only process payments with status COMPLETED
- paypal_id is used as unique transaction reference
- Always validate amount and items before saving
- This endpoint should be called after PayPal approval


---
# 💳 Mercado Pago - Create Preference API

This endpoint creates a Mercado Pago Checkout preference using the shopping cart sent from the frontend.

## Endpoint
POST /api/payments/payment-mp-create-preference

## Request Body
```json
{
"total": 120,
"username": "lince2000",
"email": "lince2000@gmail.com",
"cart": [
{
"id": 3,
"name": "zapatillas",
"price": 120,
"image": "https://example.com/product.png",
"quantity": 1,
"stock": 1
}
]
}
```
## Response
```json
{
"success": true,
"data": {
"preferenceId": "123456789-abc",
"orderId": "MP_lince2000_a1b2c3d4e5f6",
"total": 120
}
}
```
## Description
This endpoint:
- Creates an internal order with status PENDING
- Generates a unique externalReference
- Converts cart items into Mercado Pago items
- Creates a Mercado Pago Checkout preference
- Returns preferenceId for frontend checkout

## Frontend Usage
```json
const mp = new MercadoPago("PUBLIC_KEY");

mp.checkout({
preference: {
id: preferenceId
},
render: {
container: "#wallet-container",
label: "Pay"
}
});
```
## Flow
Frontend → Create Preference API → Backend → Order PENDING → Mercado Pago → Checkout → Webhook → Payment Confirmation

## Notes
- Never trust frontend total for payment validation
- Always confirm payments via webhook
- externalReference links payment with internal order
- Orders start in PENDING state until confirmed


# 🔔 Mercado Pago Webhook API

This endpoint receives payment notifications from Mercado Pago and processes the payment confirmation.

## Endpoint
POST /api/payments/payment-webhook

## Request Body (WebhookDTO)
This endpoint receives a webhook event from Mercado Pago containing payment, payer, and order information.
```json
{
"id": 1234567890,
"status": "approved",
"status_detail": "accredited",
"transaction_amount": 120,
"currency_id": "PEN",
"payment_method_id": "visa",
"external_reference": "MP_lince2000_abc123",
"authorization_code": "123456",
"captured": true,
"installments": 1,
"description": "Order payment",
"date_created": "2026-05-23T10:00:00Z",
"date_approved": "2026-05-23T10:01:00Z",
"payer": {
"id": "987654321",
"email": "customer@email.com"
},
"card": {
"country": "PE"
},
"additional_info": {
"items": [
{
"id": "3",
"title": "zapatillas",
"quantity": 1,
"unit_price": 120,
"picture_url": "https://example.com/image.png"
}
]
}
}
```
## Response
```json
{
"status": true,
"message": "MercadoPago sale created successfully",
"data": "1485337134069"
}
```
## Description

This endpoint:

- Receives payment notifications from Mercado Pago
- Extracts payment status and external reference
- Validates transaction data
- Updates internal order status
- Triggers business logic (order confirmation, stock update, etc.)
- Returns internal transaction identifier

## Key Fields

- id → Mercado Pago payment ID
- status → Payment status (approved, rejected, pending)
- transaction_amount → Paid amount
- external_reference → Internal order ID
- payer.email → Customer email
- additional_info.items → Purchased products

## Flow

Frontend → Create Preference → Payment → Mercado Pago → Webhook → Backend Processing → Order Updated

## Notes

- Never trust frontend payment confirmation
- Always validate using webhook data
- external_reference is used to match internal orders
- This endpoint must be publicly accessible (HTTPS recommended)

