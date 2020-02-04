.global _main

_main:
	movq $1, %rdi
	main_loop:
	    incq %rdi
	    pushq %rdi
        callq _is_prime
        popq %rdi
        cmpq $1, %rax
        je main_loop
        movq %rdi, %rsi
        callq printf
        cmpq $10000, %rdi
        jl main_loop

	ret

_is_prime:
    #rax = 0 if rdi is prime, otherwise 1
    movq $2, %rsi

    pushq %rdi
	pushq %rsi
	pushq %rax
	callq _sqrt_func
	movq %rax, %rcx
	popq %rax
	popq %rsi
	popq %rdi

    cmpq $2, %rdi
    je prime

    cmpq $3, %rdi
    je prime


    prime_loop:
		movq %rdi, %rax
		cqto
		idivq %rsi

		cmpq $0, %rdx
		je not_prime

		incq %rsi
		cmpq %rcx, %rsi
		jl prime_loop

	prime:
		movq $0, %rax
		ret

	not_prime:
		movq $1, %rax
		ret

.text
.global _sqrt_func

_sqrt_func:
	#rdi is to be sqrted
	movq $1, %rsi
	#While rdi > (rsi+1)^2
	sqrt_loop:
		addq $2, %rsi #Add 2 to save a bunch of time (twice as fast)
		movq %rsi, %rcx
		imulq %rcx, %rcx
		cmpq %rdi, %rcx
		jl sqrt_loop
	incq %rsi
	movq %rsi, %rax
	ret #This returns the closest integer above to the square root of the number (Possibly 2nd closest)

