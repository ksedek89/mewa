#include <linux/build-salt.h>
#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

BUILD_SALT;

MODULE_INFO(vermagic, VERMAGIC_STRING);
MODULE_INFO(name, KBUILD_MODNAME);

__visible struct module __this_module
__section(.gnu.linkonce.this_module) = {
	.name = KBUILD_MODNAME,
	.init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
	.exit = cleanup_module,
#endif
	.arch = MODULE_ARCH_INIT,
};

#ifdef CONFIG_RETPOLINE
MODULE_INFO(retpoline, "Y");
#endif

static const struct modversion_info ____versions[]
__used __section(__versions) = {
	{ 0xc79d2779, "module_layout" },
	{ 0x3184b6ef, "param_ops_int" },
	{ 0xd9a5ea54, "__init_waitqueue_head" },
	{ 0xa47bc098, "proc_create_data" },
	{ 0x7c1d44ed, "proc_mkdir" },
	{ 0x5c38c8e6, "tty_register_driver" },
	{ 0xa3daabc1, "tty_port_link_device" },
	{ 0x17865a8c, "tty_port_init" },
	{ 0xfb578fc5, "memset" },
	{ 0xafa0b588, "tty_set_operations" },
	{ 0x67b27ec1, "tty_std_termios" },
	{ 0x2a41486, "__tty_alloc_driver" },
	{ 0x8e8e4f19, "put_tty_driver" },
	{ 0x423bcb71, "tty_unregister_driver" },
	{ 0xb7a8281, "remove_proc_entry" },
	{ 0x3c3ff9fd, "sprintf" },
	{ 0x6a5cb5ee, "__get_free_pages" },
	{ 0xb8e7ce2c, "__put_user_8" },
	{ 0x37110088, "remove_wait_queue" },
	{ 0x1000e51, "schedule" },
	{ 0x4afb2238, "add_wait_queue" },
	{ 0xaad8c7d6, "default_wake_function" },
	{ 0x37c51da9, "tty_check_change" },
	{ 0xb2fd5ceb, "__put_user_4" },
	{ 0x6d334118, "__get_user_8" },
	{ 0x409873e3, "tty_termios_baud_rate" },
	{ 0xfd16c481, "do_SAK" },
	{ 0xeef09dd3, "__tty_insert_flip_char" },
	{ 0x3fec0328, "tty_wait_until_sent" },
	{ 0xe78ee22a, "tty_hung_up_p" },
	{ 0x4302d0eb, "free_pages" },
	{ 0x92540fbf, "finish_wait" },
	{ 0x8c26d495, "prepare_to_wait_event" },
	{ 0xfe487975, "init_wait_entry" },
	{ 0xa1c76e0a, "_cond_resched" },
	{ 0x15ba50a6, "jiffies" },
	{ 0x18656176, "tty_hangup" },
	{ 0xdecd0b29, "__stack_chk_fail" },
	{ 0xb44ad4b3, "_copy_to_user" },
	{ 0x8ddd8aad, "schedule_timeout" },
	{ 0x4e0ecf27, "current_task" },
	{ 0xc5b6f236, "queue_work_on" },
	{ 0x2d3385d3, "system_wq" },
	{ 0xa50dfa2f, "tty_insert_flip_string_fixed_flag" },
	{ 0x29bd6b67, "tty_buffer_request_room" },
	{ 0x37a0cba, "kfree" },
	{ 0x362ef408, "_copy_from_user" },
	{ 0x88db9f48, "__check_object_size" },
	{ 0xeb233a45, "__kmalloc" },
	{ 0xf36c4c1e, "tty_flip_buffer_push" },
	{ 0x69acdf38, "memcpy" },
	{ 0x3812050a, "_raw_spin_unlock_irqrestore" },
	{ 0x51760917, "_raw_spin_lock_irqsave" },
	{ 0x3eeb2322, "__wake_up" },
	{ 0xb3245cae, "tty_register_device" },
	{ 0xcf2a6966, "up" },
	{ 0x6626afca, "down" },
	{ 0x9968327e, "PDE_DATA" },
	{ 0xc6cbbc89, "capable" },
	{ 0xddce0ebf, "try_module_get" },
	{ 0x6ec536e, "module_put" },
	{ 0xb00465c, "tty_unregister_device" },
	{ 0xc5850110, "printk" },
	{ 0x2ea2c95c, "__x86_indirect_thunk_rax" },
	{ 0xbdfb6dbb, "__fentry__" },
};

MODULE_INFO(depends, "");


MODULE_INFO(srcversion, "98EF9F8F57908B7A9E46747");
